package com.kineticskunk.utilities;

import java.util.Random;

public class EmailAddresses {
	
	//updateFieldValue("DATE_OF_BIRTH", new_fomarted_birth);
	
		public String generateCompanyEmail() {
			String email = null;
			StringBuilder randoms = new StringBuilder();
			Random random = new Random();
			int min = 0;
			int max = 9;
			for(int i = 0; i < 3; i++) {
				int num = random.nextInt(max-min+1)+min;
				randoms.append(num);
			}
			email = this.getFieldValue("COMPANY_NAME").toLowerCase().charAt(0)
					+ this.getFieldValue("COMPANY_TRADE_NAME").toLowerCase()
					+ randoms
					+ "@kineticskunk.com";
			this.updateFieldValue("COMPANY_EMAIL_ADDRESS", email);
			logger.info("generated email is "+ email);
			return email;
		}

}
