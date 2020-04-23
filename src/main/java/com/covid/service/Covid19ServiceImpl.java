package com.covid.service;

import java.util.List;

import com.covid.dto.DistrictData;
import com.covid.dto.State;
import com.covid.dto.StateWiseCases;
import com.covid.entity.CovidPageVisitor;
import com.covid.repository.CovidPageVisitorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class Covid19ServiceImpl implements Covid19Service {
	private final CovidPageVisitorRepository repository;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public StateWiseCases getStateWiseCases() {
		repository.save(new CovidPageVisitor());
		StateWiseCases stateWiseCases = new StateWiseCases();

		ResponseEntity<String> response = restTemplate
				.getForEntity("https://api.covid19india.org/v2/state_district_wise.json", String.class);

		List<State> stateList = null;
		if (response.getBody() != null) {
			String str = null;
			try {

				TypeFactory typeFactory = objectMapper.getTypeFactory();
				CollectionType collectionType = typeFactory.constructCollectionType(List.class, State.class);
				stateList = objectMapper.readValue(response.getBody(), collectionType);

			} catch (JsonProcessingException e) {
				log.error(str + "responseData" + stateWiseCases);
				log.error("error:::" + e);
			} catch (Exception e) {
				log.error(str + "responseData" + stateWiseCases);
			}
		}

		return buildTotals(stateList);
	}

	private StateWiseCases buildTotals(List<State> stateList) {
		StateWiseCases stateWise = new StateWiseCases();
		int natRecovered = 0;
		int natDeceased = 0;
		int natActive = 0;
		int natConfirmed = 0;

		int stateRecovered;
		int stateDeceased;
		int stateActive;
		int stateConfirmed;

		for (State state : stateList) {
			stateRecovered = 0;
			stateDeceased = 0;
			stateActive = 0;
			stateConfirmed = 0;
			for (DistrictData dist : state.getDistrictData()) {
				stateRecovered = stateRecovered + dist.getRecovered();
				stateDeceased = stateDeceased + dist.getDeceased();
				stateActive = stateActive + dist.getActive();
				stateConfirmed = stateConfirmed + dist.getConfirmed();

				natRecovered = natRecovered + dist.getRecovered();
				natDeceased = natDeceased + dist.getDeceased();
				natActive = natActive + dist.getActive();
				natConfirmed = natConfirmed + dist.getConfirmed();
			}

			state.setActive(stateActive);
			state.setRecovered(stateRecovered);
			state.setConfirmed(stateConfirmed);
			state.setDeceased(stateDeceased);
		}
		stateWise.setActive(natActive);
		stateWise.setRecovered(natRecovered);
		stateWise.setConfirmed(natConfirmed);
		stateWise.setDeceased(natDeceased);
		stateWise.setStates(stateList);
		return stateWise;

	}
}
