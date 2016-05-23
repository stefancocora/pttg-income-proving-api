Feature: Failure - Category A Financial Requirement (with dependents - monthly pay)

  Requirement to meet Category A
  Applicant or Sponsor has received < 6 consecutive monthly payments from the same employer over the 182 day period prior to the Application Raised Date

  Financial income regulation to pass this Feature File
  Income required Amount no Dependent Child = £18600 (£1550 per month or above)
  Additional funds for 1 Dependent Child = £3800 on top of employment threshold
  Additional funds for EVERY subsequent dependent child = £2400 on top of employment threshold per child

  Financial income calculation to pass this Feature File
  Income required amount + 1 dependant amount + (Additional dependant amount * number of dependants)/12 = Gross Monthly Income is < Threshold in any one of the 6 payments in the 182 days prior to the Application Raised Date

  1 Dependent Child - £18600+£3800/12 = £1866.67
  2 Dependent Children - £18600+£3800+£2400/12 = £2066.67
  3 Dependent Children - £18600+£3800+(£2400*2)/12 = £2266.67
  4 Dependent Children - £18600+£3800+(£2400*3)/12 = £2466.67
  5 Dependent Children - £18600+£3800+(£2400*4)/12 = £2666.67
  7 Dependent Children - £18600+£3800+(£2400*6)/12 = £3066.67
  ETC

#New scenario - Added in
  Scenario: Shelly does not meet the Category A Financial Requirement (She has earned < the Cat A financial threshold)

  Pay date 15th of the month
  Before day of Application Raised Date
  She has 4 Canadian dependants
  She earns £2250.00 Monthly Gross Income EVERY of the 6 months

    		Given A service is consuming the Income Proving TM Family API
    		When the Income Proving TM Family API is invoked with the following:
      			| NINO                    | SP123456B  |
      			| Application Raised Date | 2015-02-03 |
      			| Dependants              | 4 |

    		Then The Income Proving TM Family API provides the following result:
      			| HTTP Status                           | 200        |
      			| Financial requirement met             | false      |
      			| Failure reason                        | MONTHLY_VALUE_BELOW_THRESHOLD      |
      			| Individual title                      | Ms         |
      			| Individual forename                   | Shelly     |
      			| Individual surname                    | Patel      |
      			| Application Raised to date            | 2014-08-05 |
      			| Application Raised date               | 2015-02-03 |
      			| National Insurance Number             | SP123456B  |

#New scenario - Added in
  Scenario: Brian does not meet the Category A Financial Requirement (He has earned < the Cat A financial threshold)

  Pay date 10th of the month
  On the same day of Application Raised Date
  He has 2 Thai dependants
  He earns £1416.67 Monthly Gross Income EVERY of the 6 months prior to the Application Raised Date

    		Given A service is consuming the Income Proving TM Family API
    		When the Income Proving TM Family API is invoked with the following:
      			| NINO                    | BS123456B  |
      			| Application Raised Date | 2015-02-10 |
      			| Dependants              | 2 |

    		Then The Income Proving TM Family API provides the following result:
      			| HTTP Status                           | 200        |
      			| Financial requirement met             | false      |
      			| Failure reason                        | MONTHLY_VALUE_BELOW_THRESHOLD      |
      			| Individual title                      | Mr         |
      			| Individual forename                   | Brian      |
      			| Individual surname                    | Sinclair   |
      			| Application Raised to date            | 2014-08-12 |
      			| Application Raised date               | 2015-02-10 |
      			| National Insurance Number             | BS123456B  |


#New scenario - Added in SD102
  Scenario: Steve does not meet the Category A employment duration Requirement (He has worked for his current employer for only 5 months)

  Pay date 3rd of the month
  On same day of Application Raised Date
  He has 3 Thai dependants
  He earns £2916.67 Monthly Gross Income BUT for only 5 months prior to the Application Raised Date
  He worked for a different employer before his current employer

    		Given A service is consuming the Income Proving TM Family API
    		When the Income Proving TM Family API is invoked with the following:
      			| NINO                    | SY987654C  |
      			| Application Raised Date | 2015-09-03 |
      			| Dependants              | 3          |

    		Then The Income Proving TM Family API provides the following result:
      			| HTTP Status                           | 200        |
      			| Financial requirement met             | false      |
      			| Failure reason                        | NOT_ENOUGH_RECORDS      |
      			| Individual title                      | Mr         |
      			| Individual forename                   | Steve      |
      			| Individual surname                    | Yu         |
      			| Application Raised to date            | 2015-03-05 |
      			| Application Raised date               | 2015-09-03 |
      			| National Insurance Number             | SY987654C  |


#New scenario - Added in SD102
#  Scenario: Scarlett Jones does not meets the Category A Financial Requirement with 3 dependants
#
#  Pay date 2nd of the month
#  Before day of Application Raised Date
#  He earns £3333.33 Monthly Gross Income EVERY of the 6 months prior to the Application Raised Date
#  He has 3 Dependants Child
#
#
#                Given A service is consuming the Income Proving TM Family API
#                When the Income Proving TM Family API is invoked with the following:
#                    | NINO                    | SJ123456C  |
#                    | Application Raised Date | 03/01/2016 |
#                    | Dependants              | 3          |
#
#                Then The Income Proving TM Family API provides the following result:
#                    | HTTP Status                           | 200        |
#                    | Financial requirement met             | False      |
#                    | Failure reason                        | NON_CONSECUTIVE_MONTHS      |
#                    | Individual title                      | Miss         |
#                    | Individual forename                   | Scarlett      |
#                    | Individual surname                    | Jones         |
#                    | Application Raised to date            | 03/06/2015 |
#                    | Application Raised date               | 03/01/2016 |
#                    | Dependant                             | 3          |
#                    | National Insurance Number             | SJ123456C  |
