package com.mircoservice.fontservice.api.util;

public class FdHttpClientFallback implements FdHttpClient {


		@Override
		public String syncFont1Feign(String jsonparam) {
			// TODO Auto-generated method stub
			return "is syncFont1fallback!"+jsonparam;
		}

		@Override
		public String syncFont2Feign() {
			// TODO Auto-generated method stub
			return "is syncFont2fallback!";
		}

	
}
