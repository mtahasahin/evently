import useAuth from "../app/hooks/useAuth";
import DefaultLayout from "../app/components/layouts/DefaultLayout/DefaultLayout";
import ProfilePage from "../app/components/templates/profile/ProfilePage";

export default function Profile() {
    const {user} = useAuth();
    return (user && <ProfilePage username={user.username}/>)
}

Profile.getLayout = page => DefaultLayout(page)
