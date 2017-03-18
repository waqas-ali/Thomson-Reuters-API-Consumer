package com.fx.ws.consumers;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.axis2.databinding.types.HexBinary;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.fx.model.AuthorizationToken;
import com.reuters.www.ns._2006._05._01.webservices.rkd.common_1.ApplicationIDType;
import com.reuters.www.ns._2006._05._01.webservices.rkd.common_1.TokenValueType;
import com.reuters.www.ns._2006._05._01.webservices.rkd.tokenmanagement_1.CreateImpersonationToken_Request_3;
import com.reuters.www.ns._2006._05._01.webservices.rkd.tokenmanagement_1.CreateImpersonationToken_Request_3E;
import com.reuters.www.ns._2006._05._01.webservices.rkd.tokenmanagement_1.CreateServiceToken_Request_1;
import com.reuters.www.ns._2006._05._01.webservices.rkd.tokenmanagement_1.CreateServiceToken_Request_1E;
import com.reuters.www.ns._2006._05._01.webservices.rkd.tokenmanagement_1.CreateServiceToken_Response_1;
import com.reuters.www.ns._2006._05._01.webservices.rkd.tokenmanagement_1.CreateToken_Response;
import com.reuters.www.ns._2006._05._01.webservices.rkd.tokenmanagement_1.PasswordType;
import com.reuters.www.ns._2006._05._01.webservices.rkd.tokenmanagement_1.ServiceCredentialsGroup;
import com.reuters.www.ns._2006._05._01.webservices.rkd.tokenmanagement_1.TokenExpirationType;
import com.reuters.www.ns._2006._05._01.webservices.rkd.tokenmanagement_1.UsernameType;
import com.reuters.www.ns._2006._05._01.webservices.rkd.tokenmanagement_1.httpsandanonymous.TokenManagement_1Stub;

public class TokenManagmentWSConsumer {
	public static Logger log = Logger.getLogger(TokenManagmentWSConsumer.class
			.getName());
	private String userId;
	private String password;
	private String appId;

	public TokenManagmentWSConsumer(String userId, String password, String appId)
			throws Exception {
		this.userId = userId;
		this.password = password;
		this.appId = appId;
	}

	public AuthorizationToken getToken() {
		try {

			TokenManagement_1Stub stub = new TokenManagement_1Stub();
			stub._getServiceClient().engageModule("addressing");

			CreateServiceToken_Request_1E cstReqE = this
					.getServiceTokenRequestE();

			CreateServiceToken_Response_1 response = stub.createServiceToken_1(
					cstReqE, null);

			CreateToken_Response res = response
					.getCreateServiceToken_Response_1();

			AuthorizationToken token = this.parseResponse(res);

			return token;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	private CreateServiceToken_Request_1E getServiceTokenRequestE() {

		CreateServiceToken_Request_1 req = new CreateServiceToken_Request_1();
		req.setServiceCredentialsGroup(this.getServiceCredentialGroup());
		CreateServiceToken_Request_1E cstReqE = new CreateServiceToken_Request_1E();
		cstReqE.setCreateServiceToken_Request_1(req);

		return cstReqE;
	}

	private ServiceCredentialsGroup getServiceCredentialGroup() {
		ApplicationIDType idType = new ApplicationIDType();
		idType.setApplicationIDType(appId);

		PasswordType pt = new PasswordType();
		pt.setPasswordType(password);

		UsernameType ut = new UsernameType();
		ut.setUsernameType(userId);

		ServiceCredentialsGroup grp = new ServiceCredentialsGroup();
		grp.setApplicationID(idType);
		grp.setPassword(pt);
		grp.setUsername(ut);

		return grp;
	}

	private AuthorizationToken parseResponse(CreateToken_Response response) {

		TokenValueType tvType = response.getToken();
		HexBinary hex = tvType.getTokenValueType();
		String tokenString = hex.toString();

		TokenExpirationType teType = response.getExpiration();
		Calendar cal = teType.getTokenExpirationType();
		Date expDate = cal.getTime();
		Date createdDate = new Date();

		AuthorizationToken token = new AuthorizationToken();
		token.setCreatedDate(createdDate);
		token.setExpirationDate(expDate);
		token.setTokenString(tokenString);

		log.info("Token Info : " + tokenString + " , Expiration Time : "
				+ expDate.toString());

		return token;
	}
}
