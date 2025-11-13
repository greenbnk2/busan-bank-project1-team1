package kr.co.api.flobankapi.mapper.admin;


import kr.co.api.flobankapi.dto.ProductDTO;
import kr.co.api.flobankapi.dto.ProductLimitDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {


    // 상품 등록
    int insertProduct(ProductDTO dto);
    int insertProductLimits(List<ProductLimitDTO> limits);

//    int updateProduct(ProductDTO dto);          // 정보 수정
//    int updateProductStatus(String dpstId, int status); // 상태 변경
//    int approveProduct(String dpstId);          // 최종 승인

}
