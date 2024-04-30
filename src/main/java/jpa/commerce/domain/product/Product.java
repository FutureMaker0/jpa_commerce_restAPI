package jpa.commerce.domain.product;

import jakarta.persistence.*;
import jpa.commerce.domain.Category;
import jpa.commerce.exception.StockUnderZeroException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "products")
    private List<Category> categories = new ArrayList<Category>();

    //@JoinColumn(name = "uploadFile_id") // UploadFile 테이블에 외래키로 매핑될 컬럼 이름
    //@JoinColumn(name = "uploadFileName")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UploadFile uploadFile;

    //@JoinColumn(name = "imageFile_id")
//    @JoinTable(name = "product_imageFiles",
//            joinColumns = @JoinColumn(name = "imageFile_id"),
//            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @OneToMany(cascade = CascadeType.ALL)
    private List<UploadFile> imageFileList = new ArrayList<>();



    //== 비즈니스 로직 ==// 비즈니스 로직의 적절한 위치에 대해서는 충분한 고민이 필요.
    // 재고충당 및 주문취소 시 재고수량 원복
    public void plusStock(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }

    // 주문발생 시 재고수량 감소
    public void minusStock(int stockQuantity) {
        int restStock = this.stockQuantity - stockQuantity;
        if (restStock < 0) {
            throw new StockUnderZeroException("재고 수량이 부족합니다.");
        }
        this.stockQuantity = restStock;
    }

//    /**
//     * 상품 삭제 로직
//     */
//    public void deleteProduct() {
//
//    }

}
