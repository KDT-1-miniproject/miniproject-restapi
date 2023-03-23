package shop.mtcoding.miniproject2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.miniproject2.dto.customerService.CustomerServDto;
import shop.mtcoding.miniproject2.dto.customerService.CustomerServDto.CompanyCustomerServiceDto;
import shop.mtcoding.miniproject2.dto.customerService.CustomerServDto.PersonCustomerServiceDto;
import shop.mtcoding.miniproject2.model.CustomerServRepository;

@Service
@RequiredArgsConstructor
public class CSService {
    private final CustomerServRepository customerServRepository;

    @Transactional
    public CustomerServDto 고객센터() {

        // DB에서 고객센터 데이터 조회하기
        List<CompanyCustomerServiceDto> companyCS = customerServRepository.findAllcompanyCS();
        List<PersonCustomerServiceDto> personCS = customerServRepository.findAllpersonCS();

        // 데이터를 customerServiceDto 객체로 매핑하기
        CustomerServDto customerService = new CustomerServDto();
        customerService.setCompanyCS(companyCS);
        customerService.setPersonCS(personCS);

        return customerService;
    }

}
