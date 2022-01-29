package com.example.cryptocurrency.domain.use_case.get_coins

import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.data.remote.dto.toCoin
import com.example.cryptocurrency.domain.model.Coin
import com.example.cryptocurrency.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {

    // we return a flow because we want to emit multiple values
    // loading, successfull, error states etc. one step at a time
    // emit data i.e. list of coins
    operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading())
            val coins = repository.getCoins().map {
                it.toCoin()
            }
            emit(Resource.Success(coins))

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error occurred"))

        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Couldn't reach server. Check you internet connection."
                )
            )

        }
    }
}