package org.buaa.ly.MyCar.http.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.entity.Store;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreDTO extends DTOBase {

    Integer id;

    String name;

    String address;

    String phone;

    Integer status;

    public Store build() {
        return build(this, Store.class);
    }

    static public StoreDTO build(Store store) {
        return build(store, StoreDTO.class);
    }

    static public List<StoreDTO> build(List<Store> stores) {
        return build(stores, StoreDTO.class);
    }

}
