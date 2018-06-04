package cin.ufpe.br.microbit_car_assist;

import org.junit.Test;

import cin.ufpe.br.microbit_car_assist.domain.executor.Executor;
import cin.ufpe.br.microbit_car_assist.domain.executor.MainThread;
import cin.ufpe.br.microbit_car_assist.domain.interactor.GetHoles;
import cin.ufpe.br.microbit_car_assist.storage.HoleRepository;
import cin.ufpe.br.microbit_car_assist.storage.HoleRepositoryImpl;

import static org.mockito.Mockito.mock;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class HoleTest {

    private HoleRepository mRepository;
    private Executor mExecutor;
    private MainThread mMainThread;


    public void startUp(){
        mRepository = mock(HoleRepositoryImpl.class);

                new HoleRepositoryImpl();
    }


    @Test
    public void testHolesFound() throws Exception {

//        List<Hole> holes = new ArrayList<Hole>(Collections.singletonList(new Hole(10,10,"12/05/10")));
//
//        when(mRepository.getHoles())
//                .thenReturn(holes);
//
//        GetHolesInteractor interactor = new GetHolesInteractor(mExecutor, mMainThread, mCallback, mRepository);
//
//        interactor.run();
//
//        Mockito.verify(mRepository).getHoles();
//        Mockito.verifyNoMoreInteractions(mRepository);
//        Mockito.verify(mCallback).onHolesLoaded(holes);
    }
}