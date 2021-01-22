package com.flaviomacedo.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {

	public static String decodeParam(String string) {
		try {
			return URLDecoder.decode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public static List<Integer> decodeIntList(String string) {
		String[] vetor = string.split(",");
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < vetor.length; i++) {
			list.add(Integer.parseInt(vetor[i]));
		}
		return list;
//		return Arrays.asList(string.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	}
}
