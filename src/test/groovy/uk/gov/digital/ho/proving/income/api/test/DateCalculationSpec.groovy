package uk.gov.digital.ho.proving.income.api.test

import spock.lang.Specification
import uk.gov.digital.ho.proving.income.api.IncomeValidator


class DateCalculationSpec extends Specification {

    def "Check we get 1 month difference between 23/01/2015 and 23/02/2015"() {

        given:
        Date from = getDate(2015,01,23)
        Date to = getDate(2015,02,23)

        when:
        long months = IncomeValidator.getDifferenceInMonthsBetweenDates(to, from)

        then:
        months == 1

    }

    def "Check we get 1 month difference between 14/07/2015 and 17/06/2015"() {

        given:
        Date from = getDate(2015,06,17)
        Date to = getDate(2015,07,14)

        when:
        long months = IncomeValidator.getDifferenceInMonthsBetweenDates(to, from)

        then:
        months == 1

    }


    def "Check we get 1 month difference between 23/01/2015 and 25/02/2015"() {

        given:
        Date from = getDate(2015,01,23)
        Date to = getDate(2015,02,20)

        when:
        long months = IncomeValidator.getDifferenceInMonthsBetweenDates(to, from)

        then:
        months == 1

    }

    def "Check we get 1 month difference between 1/01/2015 and 28/02/2015"() {

        given:
        Date from = getDate(2015,01,1)
        Date to = getDate(2015,02,28)

        when:
        long months = IncomeValidator.getDifferenceInMonthsBetweenDates(to, from)

        then:
        months == 1

    }


    def "Check we get 2 month difference between 1/12/2014 and 28/02/2015"() {

        given:
        Date from = getDate(2014,12,1)
        Date to = getDate(2015,02,28)

        when:
        long months = IncomeValidator.getDifferenceInMonthsBetweenDates(to, from)

        then:
        months == 2

    }

    def "Check we get 14 month difference between 1/12/2014 and 28/02/2016"() {

        given:
        Date from = getDate(2014,12,1)
        Date to = getDate(2016,02,28)

        when:
        long months = IncomeValidator.getDifferenceInMonthsBetweenDates(to, from)

        then:
        months == 14

    }

    Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        return cal.getTime()
    }

}
