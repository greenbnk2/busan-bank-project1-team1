package kr.co.api.flobankapi.service.admin;

import kr.co.api.flobankapi.dto.ProductDTO;
import kr.co.api.flobankapi.dto.ProductLimitDTO;
import kr.co.api.flobankapi.mapper.admin.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductMapper productMapper;

    @Transactional
    public void insertProduct(ProductDTO dto, List<ProductLimitDTO> limits) {

        // 1) 상품 기본정보 INSERT
        productMapper.insertProduct(dto);

        // 2) 트리거 + selectKey 로 자동 들어옴
        String dpstId = dto.getDpstId();

        // 3) 통화별 최소/최대 INSERT
        if (limits != null && !limits.isEmpty()) {

            // 각 통화별 limit에 dpst_id 세팅
            for (ProductLimitDTO limit : limits) {
                limit.setLmtDpstId(dpstId);
            }

            // mapper 호출
            productMapper.insertProductLimits(limits);
        }
    }
}