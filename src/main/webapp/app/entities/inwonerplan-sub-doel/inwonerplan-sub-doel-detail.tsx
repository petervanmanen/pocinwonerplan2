import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './inwonerplan-sub-doel.reducer';

export const InwonerplanSubDoelDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const inwonerplanSubDoelEntity = useAppSelector(state => state.inwonerplanSubDoel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="inwonerplanSubDoelDetailsHeading">Inwonerplan Sub Doel</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{inwonerplanSubDoelEntity.id}</dd>
          <dt>
            <span id="code">Code</span>
          </dt>
          <dd>{inwonerplanSubDoelEntity.code}</dd>
          <dt>
            <span id="naam">Naam</span>
          </dt>
          <dd>{inwonerplanSubDoelEntity.naam}</dd>
          <dt>
            <span id="actief">Actief</span>
          </dt>
          <dd>{inwonerplanSubDoelEntity.actief ? 'true' : 'false'}</dd>
          <dt>Aandachtspunt</dt>
          <dd>{inwonerplanSubDoelEntity.aandachtspunt ? inwonerplanSubDoelEntity.aandachtspunt.id : ''}</dd>
          <dt>Ontwikkelwens</dt>
          <dd>{inwonerplanSubDoelEntity.ontwikkelwens ? inwonerplanSubDoelEntity.ontwikkelwens.id : ''}</dd>
          <dt>Inwonerplan</dt>
          <dd>{inwonerplanSubDoelEntity.inwonerplan ? inwonerplanSubDoelEntity.inwonerplan.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/inwonerplan-sub-doel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/inwonerplan-sub-doel/${inwonerplanSubDoelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default InwonerplanSubDoelDetail;
