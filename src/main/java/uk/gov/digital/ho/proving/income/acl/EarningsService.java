package uk.gov.digital.ho.proving.income.acl;

import uk.gov.digital.ho.proving.income.domain.Application;

import java.time.LocalDate;

/**
 * Created by andrewmoores on 17/03/2016.
 */
public interface EarningsService {
    Application lookup(String nino, LocalDate applicationDate);
    Application lookup(String nino, LocalDate fromDate, LocalDate toDate);
}
