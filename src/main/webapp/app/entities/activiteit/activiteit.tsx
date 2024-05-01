import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './activiteit.reducer';

export const Activiteit = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const activiteitList = useAppSelector(state => state.activiteit.entities);
  const loading = useAppSelector(state => state.activiteit.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="activiteit-heading" data-cy="ActiviteitHeading">
        Activiteiten
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/activiteit/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Activiteit
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {activiteitList && activiteitList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('code')}>
                  Code <FontAwesomeIcon icon={getSortIconByFieldName('code')} />
                </th>
                <th className="hand" onClick={sort('naam')}>
                  Naam <FontAwesomeIcon icon={getSortIconByFieldName('naam')} />
                </th>
                <th className="hand" onClick={sort('actiehouder')}>
                  Actiehouder <FontAwesomeIcon icon={getSortIconByFieldName('actiehouder')} />
                </th>
                <th className="hand" onClick={sort('afhandeltermijn')}>
                  Afhandeltermijn <FontAwesomeIcon icon={getSortIconByFieldName('afhandeltermijn')} />
                </th>
                <th className="hand" onClick={sort('actief')}>
                  Actief <FontAwesomeIcon icon={getSortIconByFieldName('actief')} />
                </th>
                <th>
                  Aanbod <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {activiteitList.map((activiteit, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/activiteit/${activiteit.id}`} color="link" size="sm">
                      {activiteit.id}
                    </Button>
                  </td>
                  <td>{activiteit.code}</td>
                  <td>{activiteit.naam}</td>
                  <td>{activiteit.actiehouder}</td>
                  <td>{activiteit.afhandeltermijn}</td>
                  <td>{activiteit.actief ? 'true' : 'false'}</td>
                  <td>
                    {activiteit.aanbods
                      ? activiteit.aanbods.map((val, j) => (
                          <span key={j}>
                            <Link to={`/aanbod/${val.id}`}>{val.id}</Link>
                            {j === activiteit.aanbods.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/activiteit/${activiteit.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/activiteit/${activiteit.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/activiteit/${activiteit.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Activiteits found</div>
        )}
      </div>
    </div>
  );
};

export default Activiteit;
