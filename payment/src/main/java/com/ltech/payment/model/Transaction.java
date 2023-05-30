package com.ltech.payment.model;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="transaction", indexes = {@Index(name="idx_third_party", columnList = "third_party_id,third_party_type")})
public class Transaction {
    @Id
    private String id;
    private Long price;
    @Column(length = 50)
    private String currency;
    @Column(length = 50)
    private String status;
    @Column(name="user_key")
    private String userKey;
    @Column(name="third_party_id")
    private String thirdPartyId;
    @Column(name="third_party_type", length = 50)
    private String thirdPartyType;
}
