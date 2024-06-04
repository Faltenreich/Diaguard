package com.faltenreich.diaguard.shared.networking

class NetworkingFakeClient : NetworkingClient {

    override suspend fun request(url: String): String {
        return "{}"
    }
}