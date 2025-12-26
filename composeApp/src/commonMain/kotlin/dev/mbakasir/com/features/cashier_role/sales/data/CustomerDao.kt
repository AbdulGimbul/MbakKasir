package dev.mbakasir.com.features.cashier_role.sales.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(customers: List<CustomerEntity>)

    @Query("SELECT * FROM customers")
    fun getCustomers(): Flow<List<CustomerEntity>>
    
    @Query("SELECT * FROM customers")
    suspend fun getCustomersList(): List<CustomerEntity>

    @Query("DELETE FROM customers")
    suspend fun deleteAllCustomers()
}
