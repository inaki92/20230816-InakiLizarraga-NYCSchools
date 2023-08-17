package com.inaki.a20230816_inakilizarraga_nycschools.utils

class NullBodyException(message: String? = "Null body in response") : Exception(message)
class FailResponseException(message: String?) : Exception(message)
class NoNetworkAvailable(message: String? = "No internet connection available") : Exception(message)
class NoSchoolSelectedException(message: String? = "No school has been selected") : Exception(message)
class NoSchoolDBNException(message: String? = "School has no dbn") : Exception(message)

