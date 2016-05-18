Feature: Validation of the API fields and data

  National Insurance Numbers (NINO) - Format and Security: A NINO is made up of two letters, six numbers and a final letter (which is always A, B, C, or D)
  Date formats: Format should be dd/mm/yyyy

###################################### Section - Validation on the NINO ######################################

  Scenario: The API is not provided with an NINO (National Insurance Number)
      Given A service is consuming the Income Proving TM Family API
      When the Income Proving TM Family API is invoked with the following:
        | NINO                    |            |
        | Application Raised Date | 01/01/2015 |
      Then The Income Proving TM Family API provides the following result:
        | HTTP Status                           | 404        |
        |Status code                                  | 0008               |
        |Status message                               | Resource not found |

  Scenario: The API provides incorrect National Insurance Number prefixed with two digits
    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | 11123456A  |
      | Application Raised Date | 01/01/2015 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 400        |
      | Status code                                  | 0004         |
      | Status message                               | Invalid NINO |

  Scenario: The API provides incorrect National Insurance Number with two characters in the middle
   Given A service is consuming the Income Proving TM Family API
   When the Income Proving TM Family API is invoked with the following:
      | NINO                    | QQ12HR56A  |
      | Application Raised Date | 01/01/2015 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 400        |
      | Status code                                  | 0004         |
      | Status message                               | Invalid NINO |

  Scenario: The API provides incorrect National Insurance Number with the last digit being a number
    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | QQ1235560  |
      | Application Raised Date | 01/01/2015 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 400        |
      | Status code                                  | 0004         |
      | Status message                               | Invalid NINO |

  Scenario: The API provides incorrect National Insurance Number with the last digit not being a valid character
    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | QQ123556E  |
      | Application Raised Date | 01/01/2015 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 400        |
      | Status code                                  | 0004         |
      | Status message                               | Invalid NINO |

  Scenario: The API provides incorrect National Insurance Number as not 9 characters
   Given A service is consuming the Income Proving TM Family API
   When the Income Proving TM Family API is invoked with the following:
     | NINO                    | QQ1235A  |
     | Application Raised Date | 01/01/2015 |
   Then The Income Proving TM Family API provides the following result:
     | HTTP Status                           | 400        |
     | Status code                                  | 0004         |
     | Status message                               | Invalid NINO |

###################################### Section - Validation on the Application Raised Date ######################################

  Scenario: The API provides an incorrect Application Raised Date (Day format)
    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | QQ129956A  |
      | Application Raised Date | 85/01/2015 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 400        |
      | Status code                                  | 0002                               |
      | Message                               | Application raised date is invalid |

  Scenario: The API provides an incorrect Application Raised Date (Month format)
    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | QQ129956A  |
      | Application Raised Date | 01/13/2015 |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 400        |
      | Status code                                  | 0002                               |
      | Message                               | Application raised date is invalid |

  Scenario: The API provides an incorrect Application Raised Date (Year format)
    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
        | NINO                    | QQ129956A  |
        | Application Raised Date | 01/01/201H |
      Then The Income Proving TM Family API provides the following result:
        | HTTP Status                           | 400        |
        | Status code                                  | 0002                               |
        | Status message                               | Application raised date is invalid |

  Scenario: The API provides a blank Application Raised date
    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | QQ128856A  |
      | Application Raised Date |            |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 400        |
      | Status code                                  | 0008         |
      | Status message                               | Missing parameter: applicationRaisedDate |

###################################### Section - Validation on the Dependants field ######################################

  Scenario: The API provides Dependants with a character
    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | QQ128856A  |
      | Application Raised Date |            |
      | Dependant               | H          |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 400        |
      | Status code                                  | 0004                              |
      | Status message                               | Dependants cannot be more than 99 |

  Scenario: The API provides Dependants with a negative number
    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | QQ128856A  |
      | Application Raised Date | 01/01/2015 |
      | Dependant               | -3         |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 400        |
      | Status code                                  | 0004                              |
      | Sttus message                               | Dependants cannot be more than 99 |

  Scenario: The API provides Dependants with 3 digits
    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | QQ128856A  |
      | Application Raised Date | 01/01/2015 |
      | Dependant               | 100        |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 400        |
      | Status code                                  | 0004                              |
      | Status message                               | Dependants cannot be more than 99 |

###################################### Section - NINO does not exist ######################################

  Scenario: The API provides a NINO that does not exist in the system
    Given A service is consuming the Income Proving TM Family API
    When the Income Proving TM Family API is invoked with the following:
      | NINO                    | QQ128856A  |
      | Application Raised Date | 01/01/2015 |
      | Dependant               | 3          |
    Then The Income Proving TM Family API provides the following result:
      | HTTP Status                           | 404        |
      | Status code                                  | 0004                              |
      | Status message                               | Resource not found |
