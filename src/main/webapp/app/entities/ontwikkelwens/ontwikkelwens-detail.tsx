import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ontwikkelwens.reducer';

export const OntwikkelwensDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ontwikkelwensEntity = useAppSelector(state => state.ontwikkelwens.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ontwikkelwensDetailsHeading">Ontwikkelwens</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{ontwikkelwensEntity.id}</dd>
          <dt>
            <span id="code">Code</span>
          </dt>
          <dd>{ontwikkelwensEntity.code}</dd>
          <dt>
            <span id="naam">Naam</span>
          </dt>
          <dd>{ontwikkelwensEntity.naam}</dd>
          <dt>
            <span id="omschrijving">Omschrijving</span>
          </dt>
          <dd>{ontwikkelwensEntity.omschrijving}</dd>
          <dt>
            <span id="actief">Actief</span>
          </dt>
          <dd>{ontwikkelwensEntity.actief ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/ontwikkelwens" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ontwikkelwens/${ontwikkelwensEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default OntwikkelwensDetail;
