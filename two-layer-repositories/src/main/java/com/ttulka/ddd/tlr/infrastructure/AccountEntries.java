package com.ttulka.ddd.tlr.infrastructure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Repository
public interface AccountEntries extends CrudRepository<AccountEntries.AccountEntry, String> {

    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    class AccountEntry {
        @Id
        @Column
        public String iban;
        @Column
        public String currency;
        @Column
        public int ownerId;
    }
}
