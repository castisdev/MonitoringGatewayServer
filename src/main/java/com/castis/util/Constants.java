package com.castis.util;

public class Constants {

	public enum ResponseStatus {
		SUCCESS((int) 200), CREATED((int) 201), BAD_REQUEST((int) 400), INTERNAL_SERVER_ERROR((int) 500);

		private int statusCode;
		
		ResponseStatus(int statusCode) {
			this.statusCode = statusCode;
		}

		public int getStatusCode() {
			return statusCode;
		}
	}
	
	public static class ReturnCode {
		public static final String SUCCESS = "S";
		public static final String FAIL = "F";
	}
	
	public static class ES {
//		public static int MaxRowSize = 200000;
		public static int MaxRowSize = 10000;
		
		public static class IndexType {
			public static String MONTH = "M";
			public static String DAY = "D";
		}
	}
	
}
