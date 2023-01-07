package de.seifi.rechnung_common.entities;


import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "customer_fahrzeugschein")
public class CustomerFahrzeugScheinEntity extends EntityBase {

	@Id
	@GeneratedValue
    private UUID id;

	@Column(name="customer_id", nullable = true)
	private UUID customerId;


	@Type(type = "org.hibernate.type.BlobType")
	@Lob
	private byte[] imageBytes;

	private String name;

	@CreationTimestamp
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime created;

	@UpdateTimestamp
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime updated;

	public CustomerFahrzeugScheinEntity() {
    	super();

    }

	public CustomerFahrzeugScheinEntity(UUID customerId,
										byte[] imageBytes,
										String name) {
		this.customerId = customerId;
		this.imageBytes = imageBytes;
		this.name = name;
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getCustomerId() {
		return customerId;
	}

	public void setCustomerId(UUID customerId) {
		this.customerId = customerId;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public LocalDateTime getCreated() {
		return created;
	}

	@Override
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	@Override
	public LocalDateTime getUpdated() {
		return updated;
	}

	@Override
	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	public void setImage(BufferedImage image) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "PNG" /* for instance */, out);
		imageBytes = out.toByteArray();
	}

	public BufferedImage getImage() throws IOException {
		InputStream in = new ByteArrayInputStream(imageBytes);
		return ImageIO.read(in);
	}

}
