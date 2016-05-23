package uk.gov.digital.ho.proving.income.acl;

import uk.gov.digital.ho.proving.income.domain.Application;

import java.util.Date;

/**
 * Created by andrewmoores on 17/03/2016.
 */
public interface EarningsService {
    Application lookup(String nino, Date applicationDate);
    Application lookup(String nino, Date fromDate, Date toDate);
}
