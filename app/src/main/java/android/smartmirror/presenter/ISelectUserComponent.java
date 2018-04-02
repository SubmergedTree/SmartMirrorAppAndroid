package android.smartmirror.presenter;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 17.03.18.
 */

public interface ISelectUserComponent {
    void delete(int position);
    void select(int position);
    void logout();
    void newUser(String name, String prename, String username);
}
