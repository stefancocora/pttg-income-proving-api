Feature: Pass - Category A Financial Requirement (with no dependents - weekly pay)

		Requirement to meet Category A
			Applicant or Sponsor has received 26 payments from the same employer over 182 day period prior to the Application Raised Date

		Financial employment income regulation to pass this Feature File
			Applicant or Sponsor has received 26 weekly Gross Income payments of => £357.69 in the 182 day period prior to the Application Raised Date

#New Scenario -
  Scenario: Molly Henry meets the Category A Financial Requirement

		He has received 26 Weekly Gross Income payments of £470.43

    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | MH123456A  |
      | Application raised date | 29/11/2015 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                  | 200        |
      | Financial requirement met    | True       |
      | Individual title             | Ms         |
      | Individual forename          | Molly      |
      | Individual surname           | Henry      |
      | Application Raised to date   | 29/05/2015 |
      | Application Raised date      | 29/11/2015 |
      | National Insurance Number    | MH123456A  |


#New Scenario -
  Scenario: Fernando Sanchez meets the Category A Financial Requirement

    He has received 26 Weekly Gross Income payments of £357.69

    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | FS123456C  |
      | Application raised date | 10/04/2015 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                  | 200        |
      | Financial requirement met    | True       |
      | Individual title             | Mr         |
      | Individual forename          | Fernando   |
      | Individual surname           | Sanchez    |
      | Application Raised to date   | 10/04/2015 |
      | Application Raised date      | 10/04/2015 |
      | National Insurance Number    | FS123456C  |


#New Scenario -
  Scenario: Jonathan Odometey meets the Category A Financial Requirement

	He has received 26 Weekly Gross Income payments of £1000.00

    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | JO123456A  |
      | Application raised date | 28/06/2015 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                  | 200        |
      | Financial requirement met    | True       |
      | Individual title             | Mr         |
      | Individual forename          | John       |
      | Individual surname           | Odometey   |
      | Application Raised to date   | 28/12/2014 |
      | Application Raised date      | 28/06/2015 |
      | National Insurance Number    | JO123456A  |

