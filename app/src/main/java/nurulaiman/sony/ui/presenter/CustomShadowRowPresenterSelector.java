package nurulaiman.sony.ui.presenter;

import android.support.v17.leanback.supportleanbackshowcase.app.details.ShadowRowPresenterSelector;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.Presenter;

public class CustomShadowRowPresenterSelector extends ShadowRowPresenterSelector {

    private Presenter[] presenters = super.getPresenters();
    public CustomShadowRowPresenterSelector(){
        super();

        for(Presenter p:presenters){
            ListRowPresenter presenter;
            if(p instanceof ListRowPresenter){
                presenter = (ListRowPresenter)p;
                presenter.setHeaderPresenter(new UnfocusableIconHeaderItemPresenter());
            }

        }

    }
}
