package uk.gov.digital.ho.proving.income.acl;

import uk.gov.digital.ho.proving.income.domain.IncomeProvingResponse;

import java.util.Date;

public interface ApplicantService {
    IncomeProvingResponse lookup(String nino, Date applicationFromDate, Date applicationToDate);
}
