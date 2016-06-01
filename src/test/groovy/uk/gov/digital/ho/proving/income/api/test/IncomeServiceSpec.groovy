package uk.gov.digital.ho.proving.income.api.test

import groovy.json.JsonSlurper
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.acl.MongodbBackedApplicantService
import uk.gov.digital.ho.proving.income.acl.MongodbBackedEarningsService
import uk.gov.digital.ho.proving.income.api.IncomeRetrievalService
import uk.gov.digital.ho.proving.income.domain.Income
import uk.gov.digital.ho.proving.income.domain.Individual

import java.time.LocalDate
import java.time.Month

import static java.time.LocalDate.now
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

/**
 * @Author Home Office Digital
 */

class IncomeServiceSpec extends Specification {

    final String PIZZA_HUT = "Pizza Hut"
    final String BURGER_KING = "Burger King"

    def incomeRetrievalService = new IncomeRetrievalService()
    def earningsService = Mock(MongodbBackedEarningsService)
    def individualService = Mock(MongodbBackedApplicantService)
    MockMvc mockMvc = standaloneSetup(incomeRetrievalService).build()

    def setup() {
        incomeRetrievalService.setEarningsService(earningsService)
        incomeRetrievalService.setIndividualService(individualService)
    }

    String tomorrow = now().plusDays(1).format(ISO_LOCAL_DATE);
    String yesterday = now().minusDays(1).format(ISO_LOCAL_DATE);

    def "invalid from date is rejected"() {

        // 1 * earningsService.lookup(_,_) >> new TemporaryMigrationFamilyApplication(getIndividual(), new Date(), "A", true)
        // 1 * individualService.lookup(_,_,_) >> new IncomeProvingResponse(getIndividual(), getConsecutiveIncomes(), "9600", "M1")

        when:
        def response = mockMvc.perform(
            get("/incomeproving/v1/individual/AA123456A/income")
                .param("fromDate","2016-03-xx")
                .param("toDate", yesterday)
        )

        then:
        def jsonContent = new JsonSlurper().parseText(response.andReturn().response.getContentAsString())
        response.andExpect(status().isBadRequest())
        jsonContent.status.message == "Parameter error: From date is invalid"

    }

    def "invalid from and to dates are rejected"() {
        when:
        def response = mockMvc.perform(get("/incomeproving/v1/individual/AA123456A/income")
            .param("fromDate","2016-03-xx")
            .param("toDate", "2016-03-xx")
        )

        then:
        def jsonContent = new JsonSlurper().parseText(response.andReturn().response.getContentAsString())
        response.andExpect(status().isBadRequest())
        jsonContent.status.message == "Parameter error: From date is invalid"
    }

    def "invalid to dates are rejected"() {
        when:
        def response = mockMvc.perform(get("/incomeproving/v1/individual/AA123456A/income")
            .param("fromDate",yesterday)
            .param("toDate", "2016-03-xx")
        )

        then:
        def jsonContent = new JsonSlurper().parseText(response.andReturn().response.getContentAsString())
        response.andExpect(status().isBadRequest())
        jsonContent.status.message == "Parameter error: To date is invalid"
    }

    def "future from date is rejected"() {
        when:
        def response = mockMvc.perform(get("/incomeproving/v1/individual/AA123456A/income")
            .param("fromDate",tomorrow)
            .param("toDate", yesterday)
        )

        then:
        def jsonContent = new JsonSlurper().parseText(response.andReturn().response.getContentAsString())
        response.andExpect(status().isBadRequest())
        jsonContent.status.message == "Parameter error: fromDate"
    }

    def "future to date is rejected"() {
        when:
        def response = mockMvc.perform(get("/incomeproving/v1/individual/AA123456A/income")
            .param("fromDate",yesterday)
            .param("toDate", tomorrow)
        )

        then:
        def jsonContent = new JsonSlurper().parseText(response.andReturn().response.getContentAsString())
        response.andExpect(status().isBadRequest())
        jsonContent.status.message == "Parameter error: toDate"
    }

    def "future from and to dates are rejected"() {
        when:
        def response = mockMvc.perform(get("/incomeproving/v1/individual/AA123456A/income")
            .param("fromDate",tomorrow)
            .param("toDate", tomorrow)
        )

        then:
        def jsonContent = new JsonSlurper().parseText(response.andReturn().response.getContentAsString())
        response.andExpect(status().isBadRequest())
        jsonContent.status.message == "Parameter error: fromDate"
    }

    def getIndividual() {
        Individual individual = new Individual()
        individual.title = "Mr"
        individual.forename = "Duncan"
        individual.surname = "Sinclair"
        individual.nino = "AA123456A"
        individual
    }

    def getConsecutiveIncomes() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.JANUARY, 15),PIZZA_HUT , "1400" ))
        incomes.add(new Income(getDate(2015, Month.MAY, 15),PIZZA_HUT , "1600" ))
        incomes.add(new Income(getDate(2015, Month.JUNE, 15),PIZZA_HUT , "1600" ))
        incomes.add(new Income(getDate(2015, Month.APRIL, 15),PIZZA_HUT , "1600" ))
        incomes.add(new Income(getDate(2015, Month.JULY, 15),PIZZA_HUT , "1600" ))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 15),BURGER_KING , "1600" ))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 15),PIZZA_HUT , "1600" ))
        incomes.add(new Income(getDate(2015, Month.SEPTEMBER, 15),PIZZA_HUT , "1600" ))
        incomes
    }

    LocalDate getDate(int year, Month month, int day) {
        LocalDate localDate = LocalDate.of(year,month,day)
        return localDate
    }

}
