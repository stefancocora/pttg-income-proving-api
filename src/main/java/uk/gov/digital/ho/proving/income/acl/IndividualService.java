package uk.gov.digital.ho.proving.income.acl;

import uk.gov.digital.ho.proving.income.domain.IncomeProvingResponse;

import java.time.LocalDate;

public interface IndividualService {
    IncomeProvingResponse lookup(String nino, LocalDate applicationFromDate, LocalDate applicationToDate);
}
