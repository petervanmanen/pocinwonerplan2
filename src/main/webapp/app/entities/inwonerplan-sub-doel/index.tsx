import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import InwonerplanSubDoel from './inwonerplan-sub-doel';
import InwonerplanSubDoelDetail from './inwonerplan-sub-doel-detail';
import InwonerplanSubDoelUpdate from './inwonerplan-sub-doel-update';
import InwonerplanSubDoelDeleteDialog from './inwonerplan-sub-doel-delete-dialog';

const InwonerplanSubDoelRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<InwonerplanSubDoel />} />
    <Route path="new" element={<InwonerplanSubDoelUpdate />} />
    <Route path=":id">
      <Route index element={<InwonerplanSubDoelDetail />} />
      <Route path="edit" element={<InwonerplanSubDoelUpdate />} />
      <Route path="delete" element={<InwonerplanSubDoelDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InwonerplanSubDoelRoutes;
