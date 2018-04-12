package com.mircoservice.fontservice.api.service;

import java.io.InputStream;

import com.mircoservice.fontservice.api.beans.RequestPageSearchParams;
import com.mircoservice.fontservice.api.entity.SyncFont;
/*
 * create by suibin
 * API-字体同步
 * 2017-10-27
 */
public interface ApiFontService {
	
	 SyncFont getSyncFont(String fontid);
	 String inSyncFont(RequestPageSearchParams requestPageSearchParams);
	 //InputStream subFont(String fontDirPrefix, String fontCode, String subsetString, int type);
	 InputStream subFont(String fontDirPrefix, String fontCode, String subsetString, int type, boolean cache);
			
}
