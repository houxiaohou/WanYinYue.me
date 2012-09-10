package me.wanyinyue.utils;

import me.wanyinyue.dto.TabDTO;
import me.wanyinyue.model.Tab;

import org.springframework.stereotype.Component;

@Component(value = "dtoUtils")
public class DTOUtils {

	@SuppressWarnings("deprecation")
	public TabDTO getTabDTOForMainPage(Tab tab) {
		TabDTO tabDTO = new TabDTO();
		tabDTO.setId(tab.getId());
		tabDTO.setName(tab.getName());
		tabDTO.setSinger(tab.getSinger());
		tabDTO.setDate(tab.getDate().toLocaleString());
		tabDTO.setCheckedTimes(tab.getCheckedTimes());
		tabDTO.setUploadUserDisplay(tab.getUploadUser().getUserName());

		return tabDTO;
	}

}
