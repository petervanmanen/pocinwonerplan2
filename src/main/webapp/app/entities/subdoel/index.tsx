import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Subdoel from './subdoel';
import SubdoelDetail from './subdoel-detail';
import SubdoelUpdate from './subdoel-update';
import SubdoelDeleteDialog from './subdoel-delete-dialog';

const SubdoelRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Subdoel />} />
    <Route path="new" element={<SubdoelUpdate />} />
    <Route path=":id">
      <Route index element={<SubdoelDetail />} />
      <Route path="edit" element={<SubdoelUpdate />} />
      <Route path="delete" element={<SubdoelDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubdoelRoutes;
