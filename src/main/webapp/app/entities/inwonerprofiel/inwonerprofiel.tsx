import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './inwonerprofiel.reducer';

export const Inwonerprofiel = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const inwonerprofielList = useAppSelector(state => state.inwonerprofiel.entities);
  const loading = useAppSelector(state => state.inwonerprofiel.loading);

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
      <h2 id="inwonerprofiel-heading" data-cy="InwonerprofielHeading">
        Inwonerprofielen
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/inwonerprofiel/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Inwonerprofiel
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {inwonerprofielList && inwonerprofielList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('voornaam')}>
                  Voornaam <FontAwesomeIcon icon={getSortIconByFieldName('voornaam')} />
                </th>
                <th className="hand" onClick={sort('tussenvoegsel')}>
                  Tussenvoegsel <FontAwesomeIcon icon={getSortIconByFieldName('tussenvoegsel')} />
                </th>
                <th className="hand" onClick={sort('achternaam')}>
                  Achternaam <FontAwesomeIcon icon={getSortIconByFieldName('achternaam')} />
                </th>
                <th className="hand" onClick={sort('geboortedatum')}>
                  Geboortedatum <FontAwesomeIcon icon={getSortIconByFieldName('geboortedatum')} />
                </th>
                <th className="hand" onClick={sort('bsn')}>
                  Bsn <FontAwesomeIcon icon={getSortIconByFieldName('bsn')} />
                </th>
                <th>
                  Inwonerplan <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {inwonerprofielList.map((inwonerprofiel, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/inwonerprofiel/${inwonerprofiel.id}`} color="link" size="sm">
                      {inwonerprofiel.id}
                    </Button>
                  </td>
                  <td>{inwonerprofiel.voornaam}</td>
                  <td>{inwonerprofiel.tussenvoegsel}</td>
                  <td>{inwonerprofiel.achternaam}</td>
                  <td>
                    {inwonerprofiel.geboortedatum ? (
                      <TextFormat type="date" value={inwonerprofiel.geboortedatum} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{inwonerprofiel.bsn}</td>
                  <td>
                    {inwonerprofiel.inwonerplan ? (
                      <Link to={`/inwonerplan/${inwonerprofiel.inwonerplan.id}`}>{inwonerprofiel.inwonerplan.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/inwonerprofiel/${inwonerprofiel.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/inwonerprofiel/${inwonerprofiel.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/inwonerprofiel/${inwonerprofiel.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Inwonerprofiels found</div>
        )}
      </div>
    </div>
  );
};

export default Inwonerprofiel;
