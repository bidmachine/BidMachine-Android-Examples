package io.bidmachine.examples.base

enum class Status(val isLoading: Boolean, val status: String) {

    Loading(true, "Loading"),
    Loaded(false, "Loaded"),
    LoadFail(false, "Load Fail"),
    ShowFail(false, "Show Fail"),
    Impression(false, "Impression"),
    Clicked(false, "Clicked"),
    Closed(false, "Closed"),
    Rewarded(false, "Rewarded"),
    Expired(false, "Expired");

}