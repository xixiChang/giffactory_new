package top.zhangx.gif.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {

//	public static String getSHA1(String str) {
//		String generated = null;
//		try {
//			MessageDigest md = MessageDigest.getInstance("SHA-1");
//			md.update(str.getBytes());
//			byte[] bytes = md.digest();
//			StringBuilder sb = new StringBuilder();
//			for (int i = 0; i < bytes.length; i++) {
//				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
//						.substring(1));
//			}
//			generated = sb.toString();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		return generated;
//	}
	
	public static String encode(String str) {
		String generated = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			generated = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generated;
	}
}
