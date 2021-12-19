import 'tailwindcss/tailwind.css';
import '../app/styles/global.css';
import '../app/components/elements/Editor/Editor.css';
import 'cropperjs/dist/cropper.css';
import '../app/components/elements/DatePicker/DatePicker.css';
import '../app/components/elements/Select/Select.css';
import 'react-loader-spinner/dist/loader/css/react-spinner-loader.css';
import 'react-toastify/dist/ReactToastify.css';

import { AuthProvider } from '../app/context/AuthContext/AuthProvider';
import Route from '../app/components/Route';
import { Flip, ToastContainer } from 'react-toastify';

function MyApp({ Component, pageProps }) {
  // Use the layout defined at the page level, if available
  const getLayout = Component.getLayout || ((page) => page);

  return (
    <AuthProvider>
      <ToastContainer
        transition={Flip}
        hideProgressBar={true}
        theme="colored"
      />
      <Route
        protectedRoutes={[
          '/profile',
          '/edit/profile',
          '/edit/password',
          '/edit/close-account',
          '/create/event',
          '/event/[slug]/edit',
          '/event/[slug]/questions',
          '/event/[slug]/questions/edit',
          '/event/[slug]/questions/answers',
          '/event/[slug]/questions/answers/all',
          '/event/[slug]/questions/answers/[applicationId]',
          '/[username]/followers/requests',
          '/feed',
        ]}
        publicRoutes={['/login', '/signup', '/']}
      >
        {getLayout(<Component {...pageProps} />)}
      </Route>
    </AuthProvider>
  );
}

export default MyApp;
