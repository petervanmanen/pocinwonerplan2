import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Inwonerplan from './inwonerplan';
import InwonerplanDetail from './inwonerplan-detail';
import InwonerplanUpdate from './inwonerplan-update';
import InwonerplanDeleteDialog from './inwonerplan-delete-dialog';

const InwonerplanRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Inwonerplan />} />
    <Route path="new" element={<InwonerplanUpdate />} />
    <Route path=":id">
      <Route index element={<InwonerplanDetail />} />
      <Route path="edit" element={<InwonerplanUpdate />} />
      <Route path="delete" element={<InwonerplanDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InwonerplanRoutes;
