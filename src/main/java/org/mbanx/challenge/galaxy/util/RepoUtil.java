package org.mbanx.challenge.galaxy.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepoUtil {
	
	public static Pageable generatePageable(Integer pageNumber, Integer pageSize, List<String> sortFields, List<String> sortOrders) {
		
		List<Order> orders = generateOrders(sortFields, sortOrders);
		
		if(((pageNumber == null || pageNumber < 0) 
				|| (pageSize == null || pageSize < 1))
				&& (!orders.isEmpty())) {
			return getPagedSorted(0, Integer.MAX_VALUE, orders);
		}
		else if(pageNumber == null || pageNumber < 0) {
			return getUnpaged();
		}
		else if(pageSize == null || pageSize < 1) {
			return getUnpaged();
		}
		else if(orders.isEmpty()) {
			return getPagedUnsorted(pageNumber, pageSize);
		}
		else {
			return getPagedSorted(pageNumber, pageSize, orders);
		}
	}
	
	private static Pageable getUnpaged() {
		return Pageable.unpaged();
	}

	private static Pageable getPagedUnsorted(Integer pageNumber, Integer pageSize) {
		return PageRequest.of(pageNumber, pageSize);
	}

	private static Pageable getPagedSorted(Integer pageNumber, Integer pageSize, List<Order> orders) {
		return PageRequest.of(pageNumber, pageSize, Sort.by(orders));
	}
	
	private static List<String> normalizesortFields(String text) {
		List<String> norms = new ArrayList<>(); 
		if(StringUtils.isNotBlank(text)) {
			String[] splits = text.split(Pattern.quote(","));
			for (int i = 0; i < splits.length; i++) {
				norms.add(StringUtils.trim(splits[i]));
			}
		}
		return norms;
	}
	
	private static List<Order> generateOrders(List<String> sortFields, List<String> sortOrders) {
		List<Order> orders = new ArrayList<>();
		if(sortFields != null && !sortFields.isEmpty()) {
			for (int i = 0; i < sortFields.size(); i++) {
				String property = StringUtils.trim(sortFields.get(i));;
				Direction direction = Direction.ASC;
				try {
					direction = Direction.fromString(sortOrders.get(i));
				}
				catch(Exception e) {
					log.error("", e);
				}
				
				Order order = new Order(direction, property);
				orders.add(order);
			}
		}
		return orders;
	}
}
