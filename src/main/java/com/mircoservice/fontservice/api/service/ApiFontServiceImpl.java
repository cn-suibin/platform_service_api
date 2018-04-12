package com.mircoservice.fontservice.api.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mircoservice.fontservice.api.beans.RequestPageSearchParams;
import com.mircoservice.fontservice.api.entity.SyncFont;
import com.mircoservice.fontservice.api.util.BusinessException;
import com.mircoservice.fontservice.api.util.ErrorCode;
import com.mircoservice.fontservice.api.util.ErrorEnum;
import com.mircoservice.mybatis.dao.mapperex.FeeBaseExMapper;


import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.core.CMapTable;
import com.google.typography.font.tools.conversion.eot.EOTWriter;
import com.google.typography.font.tools.conversion.woff.WoffWriter;
import com.google.typography.font.tools.sfnttool.GlyphCoverage;
import com.google.typography.font.tools.subsetter.RenumberingSubsetter;
import com.google.typography.font.tools.subsetter.Subsetter;
/*
 * create by suibin
 * API-字体同步
 * 2017-10-27
 */
@Service
public class ApiFontServiceImpl implements ApiFontService {
	
	private static final Logger _log = LoggerFactory.getLogger(ApiFontServiceImpl.class);

	@Value("${font.dirPrefix}")
	private String fontDirPrefix;

	private static Map<String, byte[]> ttfCache = new LinkedHashMap<String, byte[]>(300, 0.75f){
		protected boolean removeEldestEntry(){
			return size() > 200;
		}
	};
	
	@Autowired
	FeeBaseExMapper feeBaseExMapper;
	
	public ApiFontServiceImpl() {
		super();
	}
	@Override
	public SyncFont getSyncFont(String fontid) {
		feeBaseExMapper.getDeptList();
		return null;
	}
	@Override
	public String inSyncFont(RequestPageSearchParams requestPageSearchParams) {
		

		
	    String fontId = (String) requestPageSearchParams.getSearchMap().get("fontid");
	    String appKey = requestPageSearchParams.getAppKey();
	    String userId = requestPageSearchParams.getUserId();
	    String channel = (String) requestPageSearchParams.getSearchMap().get("pwd");
	    int pageSize = requestPageSearchParams.getPageSize();
		
		System.out.println("-----tongbu chenggong-------");
		return "同步成功"+fontId+"--"+appKey+"--"+userId+"--"+channel+"--"+pageSize;
	}
	
	
	/**
	 * @param fontFile 字体下载ftp路径
	 * @param subsetString 字符子集
	 * @param type 输出字体格式 1 woff  2 eot
	 * @return
	 */
	@Override
	public InputStream subFont(String fontDirPrefix, String fontCode, String subsetString, int type,boolean cache){
		
		_log.info("----------开始拆分-------------");
		//AccessTokenRequest request = this.validAccessToken(accessToken);
		byte[] bs =null;
		if(cache) {
			bs = ttfCache.get(fontCode);
			_log.info("----------读取缓存-------------："+fontCode);
		}

		//
		//FileInputStream fis = null;
		//缓存不存在
		
		 if(bs == null){
		
			Path path =Paths.get(fontDirPrefix+fontCode);
			
			boolean pathExists =
			        Files.exists(path,
			            new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
			if(pathExists){
				try {
					bs=Files.readAllBytes(path);
					_log.info("----------读取硬盘-------------"+fontCode);
					if(cache) {
						ttfCache.put(fontCode, bs);
						_log.info("----------写入缓存-------------："+fontCode);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				_log.info("找不到文件:"+fontDirPrefix+fontCode);
			
				return null;
			}		
		
		}
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		try {
			FontFactory fontFactory = FontFactory.getInstance();
			Font[] fontArray = null;
			//java.nio.file
			


			//File  getf=new File(fontDirPrefix+fontCode);
			//if(!getf.exists()){
			//	return null;
			//}
			
			//fis = new FileInputStream(getf);//加载字体文件到流
			//bs = IOUtils.toByteArray(fis);//流转数组
			
			fontArray = fontFactory.loadFonts(bs);//加载字体数据
			Font font = fontArray[0];//生成字体对象
			List<CMapTable.CMapId> cmapIds = new ArrayList<CMapTable.CMapId>();
			cmapIds.add(CMapTable.CMapId.WINDOWS_BMP);
			Font newFont = font;
			if (subsetString != null) {
				Subsetter subsetter = new RenumberingSubsetter(newFont, fontFactory);
				subsetter.setCMaps(cmapIds, 1);
				List<Integer> glyphs = GlyphCoverage.getGlyphCoverage(font, subsetString);//查找要拆分的字体
				_log.info("要查找的文件:"+subsetString);
				subsetter.setGlyphs(glyphs);//慢在这里！！！！
				glyphs=null;
				Set<Integer> removeTables = new HashSet<Integer>();
				// Most of the following are valid tables, but we don't
				// renumber them yet, so strip
				removeTables.add(Tag.GDEF);
				removeTables.add(Tag.GPOS);
				removeTables.add(Tag.GSUB);
				removeTables.add(Tag.kern);
				removeTables.add(Tag.hdmx);
				removeTables.add(Tag.vmtx);
				removeTables.add(Tag.VDMX);
				removeTables.add(Tag.LTSH);
				removeTables.add(Tag.DSIG);
				// AAT tables, not yet defined in sfntly Tag class
				removeTables.add(Tag.intValue(new byte[] { 'm', 'o', 'r', 't' }));
				removeTables.add(Tag.intValue(new byte[] { 'm', 'o', 'r', 'x' }));
				subsetter.setRemoveTables(removeTables);
				newFont = subsetter.subset().build();//生成新字体
				subsetter=null;
			}
			//内存溢出
			if (type==1) {//woff
				
				WoffWriter ww= new WoffWriter();
				
				WritableFontData woffData=ww.convert(newFont);
				//ww=null;
				//woffData=null;
				woffData.copyTo(bos);
			} else if (type==2) {//eot
				EOTWriter xx= new EOTWriter(true);
				 WritableFontData eotData =xx.convert(newFont);
				 //xx=null;
				//eotData=null;
				eotData.copyTo(bos);
			} else if (type==3) {//SVG
				return null;
			}
			else if (type==0){//ttf
				fontFactory.serializeFont(newFont, bos);
			}else{
				
				return null;
			}
			
			newFont=null;
			fontFactory=null;
		} catch (IOException e) {
			_log.error( e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
		byte[] subfontBytes = bos.toByteArray();
		try {
			bos.close();
		} catch (IOException e) {
			_log.error( e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
		_log.info("----------拆分结束-------------");

		ByteArrayInputStream bis = new ByteArrayInputStream(subfontBytes);
		subfontBytes=null;
		return bis;
	}

	
	
	
}
