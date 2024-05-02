import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './inwonerplan-sub-doel.reducer';

export const InwonerplanSubDoel = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const inwonerplanSubDoelList = useAppSelector(state => state.inwonerplanSubDoel.entities);
  const loading = useAppSelector(state => state.inwonerplanSubDoel.loading);

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
      <h2 id="inwonerplan-sub-doel-heading" data-cy="InwonerplanSubDoelHeading">
        Inwonerplan Sub Doelen
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link
            to="/inwonerplan-sub-doel/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Inwonerplan Sub Doel
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {inwonerplanSubDoelList && inwonerplanSubDoelList.length > 0 ? (
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
                <th className="hand" onClick={sort('actief')}>
                  Actief <FontAwesomeIcon icon={getSortIconByFieldName('actief')} />
                </th>
                <th>
                  Aandachtspunt <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Ontwikkelwens <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Inwonerplan <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {inwonerplanSubDoelList.map((inwonerplanSubDoel, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/inwonerplan-sub-doel/${inwonerplanSubDoel.id}`} color="link" size="sm">
                      {inwonerplanSubDoel.id}
                    </Button>
                  </td>
                  <td>{inwonerplanSubDoel.code}</td>
                  <td>{inwonerplanSubDoel.naam}</td>
                  <td>{inwonerplanSubDoel.actief ? 'true' : 'false'}</td>
                  <td>
                    {inwonerplanSubDoel.aandachtspunt ? (
                      <Link to={`/aandachtspunt/${inwonerplanSubDoel.aandachtspunt.id}`}>{inwonerplanSubDoel.aandachtspunt.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {inwonerplanSubDoel.ontwikkelwens ? (
                      <Link to={`/ontwikkelwens/${inwonerplanSubDoel.ontwikkelwens.id}`}>{inwonerplanSubDoel.ontwikkelwens.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {inwonerplanSubDoel.inwonerplan ? (
                      <Link to={`/inwonerplan/${inwonerplanSubDoel.inwonerplan.id}`}>{inwonerplanSubDoel.inwonerplan.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/inwonerplan-sub-doel/${inwonerplanSubDoel.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/inwonerplan-sub-doel/${inwonerplanSubDoel.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/inwonerplan-sub-doel/${inwonerplanSubDoel.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Inwonerplan Sub Doels found</div>
        )}
      </div>
    </div>
  );
};

export default InwonerplanSubDoel;
