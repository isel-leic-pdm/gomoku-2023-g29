package ipl.isel.daw.gomoku.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ipl.isel.daw.gomoku.DependenciesContainer
import ipl.isel.daw.gomoku.TAG

/**
 * We use the work manager to keep the local cache warm.
 *
 * In this case we are using a single worker to fetch both the quote of day and
 * the weeks' quotes, thereby making sure network accesses are as close to each
 * other as possible.
 *
 * We could instead chain work items (a.k.a chained tasks), if their execution
 * time was expected to be larger than the maximum allowed (currently 15 minutes)
 */
class GomokuWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params)
{
    override suspend fun doWork(): Result {
        Log.v(TAG, "GomokuWorker is executing")
        val service = (applicationContext as DependenciesContainer).aboutService
        return try {
            service.getInfo()
            Result.success()
        }
        catch (e: Exception) {
            Result.failure()
        }
    }
}