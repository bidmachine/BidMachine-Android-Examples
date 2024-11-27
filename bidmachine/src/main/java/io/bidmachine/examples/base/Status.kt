package io.bidmachine.examples.base

enum class Status(val isLoading: Boolean, val status: String) {

    Requesting(true, "Requesting"),
    Requested(false, "Requested"),
    RequestFail(false, "Request Fail"),
    Loading(true, "Loading"),
    Loaded(false, "Loaded"),
    LoadFail(false, "Load Fail"),
    Closed(false, "Closed"),
    Rewarded(false, "Rewarded"),
    Expired(false, "Expired");

}