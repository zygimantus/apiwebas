package com.zygimantus.apiwebas.vaadin.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author Zygimantus
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "APIWEBAS")
public class Apiwebas implements Serializable {

		private static final long serialVersionUID = 1L;
		
		@Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;
        private String title;
        @Enumerated(EnumType.STRING)
        private Api api;
        @Enumerated(EnumType.STRING)
        private Resource resource;
        private boolean loaded;

        @CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "create_date")
        private Date createDate;

        @UpdateTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "modify_date")
        private Date modifyDate;

        public Apiwebas(Resource resource, boolean loaded) {
//        this.title = title;
                this.api = resource.getApi();
                this.resource = resource;
                this.loaded = loaded;
        }

}
