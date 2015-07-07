/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dto.LicenceDto;

import java.util.Set;

public interface LicencesService extends Service<LicenceDto> {


	Set<LicenceDto> getAllOrderedByCode();

	void save(Set<LicenceDto> dtos);
}
