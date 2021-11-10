import {useEffect} from 'react';
import {useRouter} from 'next/router';
import useAuth from "../hooks/useAuth";

export default function Route({protectedRoutes, publicRoutes, children}) {
    const router = useRouter();
    const {authenticated, loading} = useAuth();
    const pathIsProtected = protectedRoutes.indexOf(router.pathname) !== -1;
    const pathIsPublic = publicRoutes.indexOf(router.pathname) !== -1;

    useEffect(() => {

        if (!loading && !authenticated && pathIsProtected) {
            router.push('/login');
        } else if (!loading && authenticated && pathIsPublic) {
            router.push("/profile")
        }

    }, [loading, authenticated, pathIsProtected, pathIsPublic]);


    if (!authenticated && pathIsProtected) {
        return null;
    }

    if (authenticated && pathIsPublic) {
        return null;
    }

    return children;
}