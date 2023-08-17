package com.inaki.a20230816_inakilizarraga_nycschools.utils

sealed class NavDir {
    data class Main(val route: String = "mainScreen"): NavDir()
    data class Details(val route: String = "detailsScreen"): NavDir()
}
