Feature: Pass - Category A Financial Requirement (with no dependents - monthly pay)

      Requirement to meet Category A
      Applicant or Sponsor has received 6 consecutive monthly payments from the same employer over the 182 day period prior to the Application Raised Date

      Financial employment income regulation to pass this Feature File
      Applicant or Sponsor has earned 6 monthly payments => £1550 Monthly Gross Income in the 182 days prior to the Application Raised Date

#New Scenario -
  Scenario: Jon meets the Category A Financial Requirement (1)

      Pay date 15th of the month
      Before day of Application Raised Date
      He earns £1600 Monthly Gross Income EVERY of the 6 months

    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | AA345678A  |
      | Application Raised Date | 23/01/2015 |

    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 200        |
      | Financial requirement met             | True       |
      | Individual title                      | Mr         |
      | Individual forename                   | Jon        |
      | Individual surname                    | Taylor     |
      | Application Raised to date          | 23/07/2014 |
      | Application Raised date             | 23/01/2015 |
      | National Insurance Number             | AA345678A  |

#New Scenario -
  Scenario: Jon meets the Category A Financial Requirement (Caseworker enters the National Insurance Number with spaces)

      Pay date 1st of the month
      Before day of Application Raised Date
      He earns £1550 Monthly Gross Income EVERY of the 6 months

    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | AA 12 34 56 B |
      | Application Raised Date | 10/01/2015    |

    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 200        |
      | Financial requirement met             | True       |
      | Individual title                      | Mr         |
      | Individual forename                   | Jon        |
      | Individual surname                    | Taylor     |
      | Application Raised to date          | 10/07/2014 |
      | Application Raised date             | 10/01/2015 |
      | National Insurance Number             | AA123456B  |

#New Scenario -
  Scenario: Jon meets the Category A Financial Requirement (2)

      Pay date 28th of the month
      After day of Application Raised Date
      He earns £2240 Monthly Gross Income EVERY of the 6 months

    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | BB123456B |
      | Application Raised Date | 23/01/2015    |

    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 200        |
      | Financial requirement met             | True       |
      | Individual title                      | Mr         |
      | Individual forename                   | Jon        |
      | Individual surname                    | Taylor     |
      | Application Raised to date          | 23/07/2014 |
      | Application Raised date             | 23/01/2015 |
      | National Insurance Number             | BB123456B  |


#New Scenario -
  Scenario: Jon meets the Category A Financial Requirement (3)

      Pay date 23rd of the month
      On same day of Application Raised Date
      He earns £1551 Monthly Gross Income EVERY of the 6

    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | CC123456C |
      | Application Raised Date | 23/01/2015    |

    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 200        |
      | Financial requirement met             | True       |
      | Individual title                      | Mr         |
      | Individual forename                   | Jon        |
      | Individual surname                    | Taylor     |
      | Application Raised to date          | 23/07/2014 |
      | Application Raised date             | 23/01/2015 |
      | National Insurance Number             | CC123456C  |


#New Scenario -
  Scenario: Jon meets the Category A Financial Requirement (Application Raised Date provided with single numbers for the day and month)

      Pay date 1st of the month
      After day of Application Raised Date
      He earns £3210 Monthly Gross Income EVERY of the 6

    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | CC123456B |
      | Application Raised Date | 9/1/2015    |

    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 200        |
      | Financial requirement met             | True       |
      | Individual title                      | Mr         |
      | Individual forename                   | Jon        |
      | Individual surname                    | Taylor     |
      | Application Raised to date          | 09/07/2014 |
      | Application Raised date             | 09/01/2015 |
      | National Insurance Number             | CC123456B  |

#New Scenario -
  Scenario: Mark meets the Category A Financial Requirement

      Pay date 17th, for December 2014
      Pay date 30th, for October 2014
      Pay date 15th for all other months
      On different day of Application Raised Date
      He earns £1600 Monthly Gross Income EVERY of the 6

    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | AA123456A |
      | Application Raised Date | 23/1/2015    |

    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 200        |
      | Financial requirement met             | True       |
      | Individual title                      | Mr         |
      | Individual forename                   | Mark       |
      | Individual surname                    | Jones      |
      | Application Raised to date          | 23/07/2014 |
      | Application Raised date             | 23/01/2015 |
      | National Insurance Number             | AA123456A  |
