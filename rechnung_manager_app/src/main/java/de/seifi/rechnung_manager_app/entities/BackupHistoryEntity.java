package de.seifi.rechnung_manager_app.entities;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "backup_history")
public class BackupHistoryEntity extends EntityBase {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="table_name", nullable = false)
    private String tableName;

    @Column(name="backup_ts", nullable = false)
    private LocalDateTime backupTimestamp;

    @Column(name="backup_result", nullable = false)
    private Integer backupResult;

    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime created;

    @UpdateTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime updated;

    public BackupHistoryEntity() {
        super();
    }

    public BackupHistoryEntity(UUID id,
                               String tableName,
                               LocalDateTime backupTimestamp,
                               Integer backupResult) {
        super();

        this.id = id;
        this.tableName = tableName;
        this.backupTimestamp = backupTimestamp;
        this.backupResult = backupResult;
    }

    public BackupHistoryEntity(String tableName,
                               LocalDateTime backupTimestamp,
                               Integer backupResult) {
        super();

        this.tableName = tableName;
        this.backupTimestamp = backupTimestamp;
        this.backupResult = backupResult;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public LocalDateTime getBackupTimestamp() {
        return backupTimestamp;
    }

    public void setBackupTimestamp(LocalDateTime backupTimestamp) {
        this.backupTimestamp = backupTimestamp;
    }

    public Integer getBackupResult() {
        return backupResult;
    }

    public void setBackupResult(Integer backupResult) {
        this.backupResult = backupResult;
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
}
