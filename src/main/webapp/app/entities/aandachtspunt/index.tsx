import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Aandachtspunt from './aandachtspunt';
import AandachtspuntDetail from './aandachtspunt-detail';
import AandachtspuntUpdate from './aandachtspunt-update';
import AandachtspuntDeleteDialog from './aandachtspunt-delete-dialog';

const AandachtspuntRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Aandachtspunt />} />
    <Route path="new" element={<AandachtspuntUpdate />} />
    <Route path=":id">
      <Route index element={<AandachtspuntDetail />} />
      <Route path="edit" element={<AandachtspuntUpdate />} />
      <Route path="delete" element={<AandachtspuntDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AandachtspuntRoutes;
