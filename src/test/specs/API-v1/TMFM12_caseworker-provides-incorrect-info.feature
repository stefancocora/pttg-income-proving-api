#@caseworker-provides-incorrect-info
Feature: Robert is presented with an error when attempting to obtain a NINOs income information

  Scenario: Robert is unable to obtain the NINOs income details due to NOT providing the NINO field
    Given Robert is using the IPS Generic Tool
    When Robert submits a query:
      | nino      |            |
      | From Date | 2015-01-01 |
      | To Date   | 2015-06-30 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status    | 404                |
      | Status code    | 0008               |
      | Status message | Resource not found: /incomeproving/v1/individual//income |


  Scenario: Robert is unable to obtain the NINOs income details due to providing a 7 character NINO
    Given Robert is using the IPS Generic Tool
    When Robert submits a query:
      | nino      | QQ1236A  |
      | From Date | 2015-01-01 |
      | To Date   | 2015-06-30 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status    | 400                           |
      | Status code    | 0004                          |
      | Status message | Parameter error: Invalid NINO |

  Scenario: Robert is unable to obtain the NINOs income details due to NOT providing the From Date field
    Given Robert is using the IPS Generic Tool
    When Robert submits a query:
      | nino      | QQ123456A  |
      | From Date |            |
      | To Date   | 2015-06-30 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status    | 400                                      |
      | Status code    | 0004                                     |
      | Status message | Required String parameter 'fromDate' is not present |

  Scenario: Robert is unable to obtain the NINOs income details due to NOT providing the To Date field
    Given Robert is using the IPS Generic Tool
    When Robert submits a query:
      | nino      | QQ129856A  |
      | From Date | 2015-01-01 |
      | To Date   |            |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status    | 400                                      |
      | Status code    | 0004                                     |
      | Status message | Required String parameter 'toDate' is not present |

  Scenario: Robert is unable to obtain the NINOs income details due to NINO does not exist being held by the HMRC for the give NINO
    Given Robert is using the IPS Generic Tool
    When Robert submits a query:
      | nino      | PP129435A  |
      | From Date | 2015-01-01 |
      | To Date   | 2015-06-30 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status    | 400                           |
      | Status code    | 0004                          |
      | Status message | Parameter error: Invalid NINO |

  Scenario: Robert is unable to obtain the NINOs income details due to no income records being held by the HMRC for the give NINO
    Given Robert is using the IPS Generic Tool
    When Robert submits a query:
      | nino      | QQ769875A  |
      | From Date | 2015-01-01 |
      | To Date   | 2015-06-30 |
    Then The service displays the following message:
      | Error Field   | serverError             |
      | Error Message | No income records found |
