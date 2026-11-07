package com.B2BMarket.ordering.infrastructure.persistence.shoppingcart;
import com.B2BMarket.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = "id")
@Table(name = "shopping_cart")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class ShoppingCartPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private CustomerPersistenceEntity customer;

    private BigDecimal totalAmount;
    private Integer totalItems;

    @CreatedBy
    private UUID createdByUserId;

    @CreatedDate
    private OffsetDateTime createdAt;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @Version
    private Long version;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShoppingCartItemPersistenceEntity> items = new HashSet<>();

    @Builder
    public ShoppingCartPersistenceEntity(UUID id, CustomerPersistenceEntity customer,
                                         BigDecimal totalAmount, Integer totalItems,
                                         UUID createdByUserId, OffsetDateTime createdAt,
                                         OffsetDateTime lastModifiedAt, UUID lastModifiedByUserId,
                                         Long version, Set<ShoppingCartItemPersistenceEntity> items) {
        this.id = id;
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.createdByUserId = createdByUserId;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
        this.replaceItems(items);
    }

    public void replaceItems(Set<ShoppingCartItemPersistenceEntity> updatedItems) {
        if (updatedItems == null || updatedItems.isEmpty()) {
            this.setItems(new HashSet<>());
            return;
        }
        updatedItems.forEach(i -> i.setShoppingCart(this));
        this.setItems(updatedItems);
    }

    public void addItem(ShoppingCartItemPersistenceEntity item) {
        if (item == null) {
            return;
        }
        if (this.getItems() == null) {
            this.setItems(new HashSet<>());
        }
        item.setShoppingCart(this);
        this.getItems().add(item);
    }

    public UUID getCustomerId() {
        if (this.customer == null) {
            return null;
        }
        return this.customer.getId();
    }
}
