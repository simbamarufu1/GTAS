/*
 * All GTAS code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 *
 * Please see LICENSE.txt for details.
 */
package gov.gtas.services;

import gov.gtas.model.lookup.Carrier;
import gov.gtas.repository.CarrierRepository;
import gov.gtas.repository.CarrierRepositoryCustom;
import gov.gtas.vo.lookup.CarrierVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarrierServiceImpl implements CarrierService {

	@Resource
	private CarrierRepository carrierRespository;
	@Resource
	private CarrierRepositoryCustom carrierRepoCust;

	@Override
	@Transactional
	public CarrierVo create(CarrierVo carrierVo) {
		Carrier savedCarrier = carrierRespository.save(buildCarrier(carrierVo));

		return buildCarrierVo(savedCarrier);
	}

	@Override
	@Transactional
	public CarrierVo delete(Long id) {
		CarrierVo carrierVo = this.findById(id);

		if (carrierVo != null) {
			carrierRespository.delete(buildCarrier(carrierVo));

			return carrierVo;
		}

		return null;
	}

	@Override
	@Transactional
	public List<CarrierVo> findAll() {

		List<Carrier> allCarriers = (List<Carrier>) carrierRespository.findAll();

		List<CarrierVo> allCarrierVos = new ArrayList<>();

		for (Carrier carrier : allCarriers) {
			allCarrierVos.add(buildCarrierVo(carrier));
		}

		return allCarrierVos;
	}

	@Override
	@Transactional
	public CarrierVo update(CarrierVo carrierVo) {
		// validate no duplicate codes
		Carrier savedCarrier = carrierRespository.save(buildCarrier(carrierVo));

		return buildCarrierVo(savedCarrier);
	}

	@Override
	@Transactional
	public CarrierVo findById(Long id) {
		Carrier carrier = carrierRespository.findById(id).orElse(null);

		if (carrier != null) {
			return buildCarrierVo(carrier);
		}

		return null;
	}

	@Override
	@Transactional
	public CarrierVo restore(CarrierVo carrierVo) {
		Carrier restoredCarrier = carrierRepoCust.restore(buildCarrier(carrierVo));

		return buildCarrierVo(restoredCarrier);
	}

	@Override
	@Transactional
	public int restoreAll() {
		return carrierRepoCust.restoreAll();
	}

	@Override
	@Transactional
	public CarrierVo getCarrierByTwoLetterCode(String carrierCode) {
		List<Carrier> carriers = carrierRespository.getCarrierByTwoLetterCode(carrierCode);

		if (carriers != null && carriers.size() > 0) {
			return buildCarrierVo(carriers.get(0));
		}

		return null;
	}

	@Override
	@Transactional
	public CarrierVo getCarrierByThreeLetterCode(String carrierCode) {
		List<Carrier> carriers = carrierRespository.getCarrierByThreeLetterCode(carrierCode);

		if (carriers != null && carriers.size() > 0) {
			return buildCarrierVo(carriers.get(0));
		}

		return null;
	}

	private Carrier buildCarrier(CarrierVo carrierVo) {
		return new Carrier(carrierVo.getId(), carrierVo.getOriginId(), carrierVo.getName(), carrierVo.getIata(),
				carrierVo.getIcao());
	}

	private CarrierVo buildCarrierVo(Carrier carrier) {
		return new CarrierVo(carrier.getId(), carrier.getOriginId(), carrier.getName(), carrier.getIata(),
				carrier.getIcao());
	}

}
